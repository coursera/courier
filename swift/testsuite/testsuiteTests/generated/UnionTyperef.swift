import Foundation
import SwiftyJSON

public enum UnionTyperef: JSONSerializable, DataTreeSerializable {
    
    case StringMember(String)
    
    case IntMember(Int)
    case UNKNOWN$([String : AnyObject])
    
    public static func readJSON(json: JSON) -> UnionTyperef {
        let dict = json.dictionaryValue
        if let member = dict["string"] {
            return .StringMember(member.stringValue)
        }
        if let member = dict["int"] {
            return .IntMember(member.intValue)
        }
        return .UNKNOWN$(json.dictionaryObject!)
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> UnionTyperef {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        switch self {
        case .StringMember(let member):
            return ["string": member];
        case .IntMember(let member):
            return ["int": member];
        case .UNKNOWN$(let dict):
            return dict
        }
    }
}

