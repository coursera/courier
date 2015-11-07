import Foundation
import SwiftyJSON

public enum TypedDefinition: Serializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> TypedDefinition {
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(try Note.readJSON(json["definition"].required(.Dictionary).jsonValue))
        case "message":
            return .MessageMember(try Message.readJSON(json["definition"].required(.Dictionary).jsonValue))
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

public func ==(lhs: TypedDefinition, rhs: TypedDefinition) -> Bool {
    switch (lhs, rhs) {
    case (let .NoteMember(lhs), let .NoteMember(rhs)):
        return lhs == rhs
    case (let .MessageMember(lhs), let .MessageMember(rhs)):
        return lhs == rhs
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return JSON(lhs) == JSON(rhs)
    default:
        return false
    }
}
