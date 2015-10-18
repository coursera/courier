import Foundation
import SwiftyJSON

enum UnionTyperef {
    
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
    func write() -> [String : JSON] {
        switch self {
            
        case .StringMember(let member):
            return ["string": JSON(member)];
            
        case .IntMember(let member):
            return ["int": JSON(member)];
            
        case .UNKNOWN$(let dictionary):
            return dictionary
        }
    }
}

