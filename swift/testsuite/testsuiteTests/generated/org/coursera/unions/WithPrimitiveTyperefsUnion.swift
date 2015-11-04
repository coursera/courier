import Foundation
import SwiftyJSON

public struct WithPrimitiveTyperefsUnion: JSONSerializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: JSONSerializable {
        case IntMember(Int)
        case UNKNOWN$([String : JSON])
        public static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            if let member = dictionary["int"] {
                return .IntMember(member.intValue)
            }
            return .UNKNOWN$(dictionary)
        }
        public func write() -> JSON {
            switch self {
            case .IntMember(let member):
                return JSON(["int": JSON(member)]);
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    public static func read(json: JSON) -> WithPrimitiveTyperefsUnion {
        return WithPrimitiveTyperefsUnion(
            union: json["union"].json.map { Union.read($0) }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let union = self.union {
            json["union"] = union.write()
        }
        return JSON(json)
    }
}

