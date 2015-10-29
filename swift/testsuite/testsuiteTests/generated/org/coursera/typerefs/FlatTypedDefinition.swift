import Foundation
import SwiftyJSON

enum FlatTypedDefinition: JSONSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : JSON])
    
    static func read(json: JSON) -> FlatTypedDefinition {
        let dictionary = json.dictionaryValue
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(Note.read(json.jsonValue))
        case "message":
            return .MessageMember(Message.read(json.jsonValue))
        default:
            return .UNKNOWN$(dictionary)
        }
    }
    func write() -> JSON {
        switch self {
        case .NoteMember(let member):
            var json = member.write()
            json["typeName"] = "note"
            return json
        case .MessageMember(let member):
            var json = member.write()
            json["typeName"] = "message"
            return json
        case .UNKNOWN$(let json):
            return JSON(json)
        }
    }
}

func ==(lhs: FlatTypedDefinition, rhs: FlatTypedDefinition) -> Bool {
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
