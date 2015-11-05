import Foundation
import SwiftyJSON

public struct WithPrimitiveTyperefsUnion: JSONSerializable, DataTreeSerializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: JSONSerializable, DataTreeSerializable {
        case IntMember(Int)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) -> Union {
            let dict = json.dictionaryValue
            if let member = dict["int"] {
                return .IntMember(member.intValue)
            }
            return .UNKNOWN$(json.dictionaryObject!)
        }
        public func writeJSON() -> JSON {
            return JSON(self.writeData())
        }
        public static func readData(data: [String: AnyObject]) -> Union {
            return readJSON(JSON(data))
        }
        public func writeData() -> [String: AnyObject] {
            switch self {
            case .IntMember(let member):
                return ["int": member];
            case .UNKNOWN$(let dict):
                return dict
            }
        }
    }
    
    public static func readJSON(json: JSON) -> WithPrimitiveTyperefsUnion {
        return WithPrimitiveTyperefsUnion(
            union: json["union"].json.map { Union.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithPrimitiveTyperefsUnion {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}

