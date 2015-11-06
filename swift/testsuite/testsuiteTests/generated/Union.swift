import Foundation
import SwiftyJSON

public enum Union: JSONSerializable, DataTreeSerializable, Equatable {
    
    case NoteMember(Note)
    
    case MessageMember(Message)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> Union {
        let dict = json.dictionaryValue
        if let member = dict["org.coursera.records.Note"] {
            return .NoteMember(try Note.readJSON(member.jsonValue))
        }
        if let member = dict["org.coursera.records.Message"] {
            return .MessageMember(try Message.readJSON(member.jsonValue))
        }
        if let unknownDict = json.dictionaryObject {
            return .UNKNOWN$(unknownDict)
        } else {
            throw ReadError.MalformedUnion
        }
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> Union {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .NoteMember(let member):
            return ["org.coursera.records.Note": member.writeData()];
        case .MessageMember(let member):
            return ["org.coursera.records.Message": member.writeData()];
        case .UNKNOWN$(let dict):
            return dict
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
        return JSON(lhs) == JSON(rhs)
    default:
        return false
    }
}
