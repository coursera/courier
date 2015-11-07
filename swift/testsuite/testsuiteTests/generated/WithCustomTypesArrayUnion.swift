import Foundation
import SwiftyJSON

public enum WithCustomTypesArrayUnion: Serializable, Equatable {
    
    case IntMember(Int)
    
    case StringMember(String)
    
    case SimpleMember(Simple)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> WithCustomTypesArrayUnion {
        let dict = json.dictionaryValue
        if let member = dict["int"] {
            return .IntMember(try member.required(.Number).intValue)
        }
        if let member = dict["string"] {
            return .StringMember(try member.required(.String).stringValue)
        }
        if let member = dict["org.coursera.records.test.Simple"] {
            return .SimpleMember(try Simple.readJSON(try member.required(.Dictionary).jsonValue))
        }
        if let unknownDict = json.dictionaryObject {
            return .UNKNOWN$(unknownDict)
        } else {
            throw ReadError(cause: "Union must be a JSON object.")
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

public func ==(lhs: WithCustomTypesArrayUnion, rhs: WithCustomTypesArrayUnion) -> Bool {
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
