import Foundation
import SwiftyJSON

enum Union: Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : JSON])
    static func read(json: JSON) -> Union {
        let dictionary = json.dictionaryValue
        
        if let member = dictionary["org.coursera.records.Note"] {
            return .NoteMember(Note.read(member.jsonValue))
        }
        
        if let member = dictionary["org.coursera.records.Message"] {
            return .MessageMember(Message.read(member.jsonValue))
        }
        
        return .UNKNOWN$(dictionary)
    }
    func write() -> [String : JSON] {
        switch self {
            
        case .NoteMember(let member):
            return ["org.coursera.records.Note": JSON(member.write())];
            
        case .MessageMember(let member):
            return ["org.coursera.records.Message": JSON(member.write())];
            
        case .UNKNOWN$(let dictionary):
            return dictionary
        }
    }
}

func ==(lhs: Union, rhs: Union) -> Bool {
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
