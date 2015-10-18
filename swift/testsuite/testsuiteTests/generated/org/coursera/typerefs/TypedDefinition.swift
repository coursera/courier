import Foundation
import SwiftyJSON

enum TypedDefinition: Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : JSON])
    static func read(json: JSON) -> TypedDefinition {
        switch json["typeName"].stringValue {
            
        case "note":
            return .NoteMember(Note.read(json["definition"].jsonValue))
            
        case "message":
            return .MessageMember(Message.read(json["definition"].jsonValue))
        default:
            return .UNKNOWN$(json.dictionaryValue)
        }
    }
    func write() -> [String : JSON] {
        switch self {
            
        case .NoteMember(let member):
            return ["typeName": "note", "definition": JSON(member.write())];
            
        case .MessageMember(let member):
            return ["typeName": "message", "definition": JSON(member.write())];
            
        case .UNKNOWN$(let json):
            return json
        }
    }
}

func ==(lhs: TypedDefinition, rhs: TypedDefinition) -> Bool {
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
