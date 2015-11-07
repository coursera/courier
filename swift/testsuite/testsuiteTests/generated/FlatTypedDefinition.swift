import Foundation
import SwiftyJSON

public enum FlatTypedDefinition: Serializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> FlatTypedDefinition {
        let dict = json.dictionaryValue
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(try Note.readJSON(try json.required(.Dictionary).jsonValue))
        case "message":
            return .MessageMember(try Message.readJSON(try json.required(.Dictionary).jsonValue))
        default:
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Flat Typed Definition Union must be a JSON object.")
            }
        }
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .NoteMember(let member):
            var dict = member.writeData()
            dict["typeName"] = "note"
            return dict
        case .MessageMember(let member):
            var dict = member.writeData()
            dict["typeName"] = "message"
            return dict
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

public func ==(lhs: FlatTypedDefinition, rhs: FlatTypedDefinition) -> Bool {
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
