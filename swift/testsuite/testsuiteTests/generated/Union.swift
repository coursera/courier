import Foundation
import SwiftyJSON

public enum Union: Serializable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> Union {
        let dict = json.dictionaryValue
        if let member = dict["org.coursera.records.mutable.Note"] {
            return .NoteMember(try Note.readJSON(try member.required(.Dictionary).jsonValue))
        }
        if let member = dict["org.coursera.records.mutable.Message"] {
            return .MessageMember(try Message.readJSON(try member.required(.Dictionary).jsonValue))
        }
        if let unknownDict = json.dictionaryObject {
            return .UNKNOWN$(unknownDict)
        } else {
            throw ReadError(cause: "Union must be a JSON object.")
        }
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .NoteMember(let member):
            return ["org.coursera.records.mutable.Note": member.writeData()];
        case .MessageMember(let member):
            return ["org.coursera.records.mutable.Message": member.writeData()];
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

