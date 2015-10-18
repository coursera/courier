import Foundation
import SwiftyJSON

struct WithPrimitiveTyperefsUnion {
    
    let union: Union?
    
    init(union: Union?) {
        
        self.union = union
    }
    
    enum Union {
        
        case IntMember(Int)
        case UNKNOWN$([String : JSON])
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            
            if let member = dictionary["int"] {
                return .IntMember(member.intValue)
            }
            
            return .UNKNOWN$(dictionary)
        }
        func write() -> [String : JSON] {
            switch self {
                
            case .IntMember(let member):
                return ["int": JSON(member)];
                
            case .UNKNOWN$(let dictionary):
                return dictionary
            }
        }
    }
    
    static func read(json: JSON) -> WithPrimitiveTyperefsUnion {
        return WithPrimitiveTyperefsUnion(
        union: json["union"].json.map { Union.read($0) })
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let union = self.union {
            json["union"] = JSON(union.write())
        }
        
        return json
    }
}

