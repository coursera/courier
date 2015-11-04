import Foundation
import SwiftyJSON

public struct WithEmptyUnion: JSONSerializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: JSONSerializable {
        case UNKNOWN$([String : JSON])
        public static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            return .UNKNOWN$(dictionary)
        }
        public func write() -> JSON {
            switch self {
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    public static func read(json: JSON) -> WithEmptyUnion {
        return WithEmptyUnion(
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

