import Foundation
import SwiftyJSON

enum UnionTyperef: JSONSerializable {
    
    case StringMember(String)
    
    case IntMember(Int)
    case UNKNOWN$([String : JSON])
    
    static func read(json: JSON) -> UnionTyperef {
        let dictionary = json.dictionaryValue
        if let member = dictionary["string"] {
            return .StringMember(member.stringValue)
        }
        if let member = dictionary["int"] {
            return .IntMember(member.intValue)
        }
        return .UNKNOWN$(dictionary)
    }
    func write() -> JSON {
        switch self {
        case .StringMember(let member):
            return JSON(["string": JSON(member)]);
        case .IntMember(let member):
            return JSON(["int": JSON(member)]);
        case .UNKNOWN$(let dictionary):
            return JSON(dictionary)
        }
    }
}

