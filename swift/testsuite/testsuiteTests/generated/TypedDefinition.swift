import Foundation
import SwiftyJSON

public enum TypedDefinition: JSONSerializable, DataTreeSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) -> TypedDefinition {
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(Note.readJSON(json["definition"].jsonValue))
        case "message":
            return .MessageMember(Message.readJSON(json["definition"].jsonValue))
        default:
            return .UNKNOWN$(json.dictionaryObject!)
        }
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> TypedDefinition {
        return readJSON(JSON(data))
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
