import Foundation
import SwiftyJSON

public enum FortuneTelling: Serializable {
    
    case FortuneCookieMember(FortuneCookie)
    
    case MagicEightBallMember(MagicEightBall)
    
    case StringMember(String)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) throws -> FortuneTelling {
        let dict = json.dictionaryValue
        if let member = dict["org.example.FortuneCookie"] {
            return .FortuneCookieMember(try FortuneCookie.readJSON(try member.required(.Dictionary).jsonValue))
        }
        if let member = dict["org.example.MagicEightBall"] {
            return .MagicEightBallMember(try MagicEightBall.readJSON(try member.required(.Dictionary).jsonValue))
        }
        if let member = dict["string"] {
            return .StringMember(try member.required(.String).stringValue)
        }
        if let unknownDict = json.dictionaryObject {
            return .UNKNOWN$(unknownDict)
        } else {
            throw ReadError(cause: "Union must be a JSON object.")
        }
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .FortuneCookieMember(let member):
            return ["org.example.FortuneCookie": member.writeData()];
        case .MagicEightBallMember(let member):
            return ["org.example.MagicEightBall": member.writeData()];
        case .StringMember(let member):
            return ["string": member];
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

