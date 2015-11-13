import Foundation
import SwiftyJSON

public struct WithPrimitiveCustomTypesUnion: Serializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: Serializable {
        case IntMember(Int)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["int"] {
                return .IntMember(try member.required(.Number).intValue)
            }
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Union must be a JSON object.")
            }
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
    
    public static func readJSON(json: JSON) throws -> WithPrimitiveCustomTypesUnion {
        return WithPrimitiveCustomTypesUnion(
            union: try json["union"].optional(.Dictionary).json.map { try Union.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}

