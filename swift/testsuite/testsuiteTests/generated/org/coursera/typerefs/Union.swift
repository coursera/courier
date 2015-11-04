import Foundation
import SwiftyJSON

public enum Union: JSONSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : JSON])
    
    public static func read(json: JSON) -> Union {
        let dictionary = json.dictionaryValue
        if let member = dictionary["org.coursera.records.Note"] {
            return .NoteMember(Note.read(member.jsonValue))
        }
        if let member = dictionary["org.coursera.records.Message"] {
            return .MessageMember(Message.read(member.jsonValue))
        }
        return .UNKNOWN$(dictionary)
    }
    public func write() -> JSON {
        switch self {
        case .NoteMember(let member):
            return JSON(["org.coursera.records.Note": member.write()]);
        case .MessageMember(let member):
            return JSON(["org.coursera.records.Message": member.write()]);
        case .UNKNOWN$(let dictionary):
            return JSON(dictionary)
        }
    }
}

public func ==(lhs: Union, rhs: Union) -> Bool {
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
