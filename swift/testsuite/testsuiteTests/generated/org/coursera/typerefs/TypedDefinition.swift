import Foundation
import SwiftyJSON

public enum TypedDefinition: JSONSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : JSON])
    
    public static func read(json: JSON) -> TypedDefinition {
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(Note.read(json["definition"].jsonValue))
        case "message":
            return .MessageMember(Message.read(json["definition"].jsonValue))
        default:
            return .UNKNOWN$(json.dictionaryValue)
        }
    }
    public func write() -> JSON {
        switch self {
        case .NoteMember(let member):
            return JSON(["typeName": "note", "definition": member.write()]);
        case .MessageMember(let member):
            return JSON(["typeName": "message", "definition": member.write()]);
        case .UNKNOWN$(let json):
            return JSON(json)
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
        return lhs == rhs
    default:
        return false
    }
}
