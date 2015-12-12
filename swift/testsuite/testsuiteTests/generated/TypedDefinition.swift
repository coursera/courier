import Foundation
import SwiftyJSON

public enum TypedDefinition: Serializable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> TypedDefinition {
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(try Note.readJSON(try json["definition"].required(.Dictionary).jsonValue))
        case "message":
            return .MessageMember(try Message.readJSON(try json["definition"].required(.Dictionary).jsonValue))
        default:
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Typed Definition Union must be a JSON object.")
            }
        }
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .NoteMember(let member):
            return ["typeName": "note", "definition": member.writeData()];
        case .MessageMember(let member):
            return ["typeName": "message", "definition": member.writeData()];
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

