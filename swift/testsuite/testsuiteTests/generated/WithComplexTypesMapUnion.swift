import Foundation
import SwiftyJSON

public enum WithComplexTypesMapUnion: Serializable, Equatable {
    
    case IntMember(Int)
    
    case StringMember(String)
    
    case SimpleMember(Simple)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> WithComplexTypesMapUnion {
        let dict = json.dictionaryValue
        if let member = dict["int"] {
            return .IntMember(member.intValue)
        }
        if let member = dict["string"] {
            return .StringMember(member.stringValue)
        }
        if let member = dict["org.coursera.records.test.Simple"] {
            return .SimpleMember(try Simple.readJSON(member.jsonValue))
        }
        if let unknownDict = json.dictionaryObject {
            return .UNKNOWN$(unknownDict)
        } else {
            throw ReadError.MalformedUnion
        }
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .IntMember(let member):
            return ["int": member];
        case .StringMember(let member):
            return ["string": member];
        case .SimpleMember(let member):
            return ["org.coursera.records.test.Simple": member.writeData()];
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

public func ==(lhs: WithComplexTypesMapUnion, rhs: WithComplexTypesMapUnion) -> Bool {
    switch (lhs, rhs) {
    case (let .IntMember(lhs), let .IntMember(rhs)):
        return lhs == rhs
    case (let .StringMember(lhs), let .StringMember(rhs)):
        return lhs == rhs
    case (let .SimpleMember(lhs), let .SimpleMember(rhs)):
        return lhs == rhs
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return JSON(lhs) == JSON(rhs)
    default:
        return false
    }
}
