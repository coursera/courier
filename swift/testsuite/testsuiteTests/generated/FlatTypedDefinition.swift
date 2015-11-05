import Foundation
import SwiftyJSON

public enum FlatTypedDefinition: JSONSerializable, DataTreeSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) -> FlatTypedDefinition {
        let dict = json.dictionaryValue
        switch json["typeName"].stringValue {
        case "note":
            return .NoteMember(Note.readJSON(json.jsonValue))
        case "message":
            return .MessageMember(Message.readJSON(json.jsonValue))
        default:
            return .UNKNOWN$(json.dictionaryObject!)
        }
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> FlatTypedDefinition {
        return readJSON(JSON(data))
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
