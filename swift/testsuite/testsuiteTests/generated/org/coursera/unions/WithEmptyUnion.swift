import Foundation
import SwiftyJSON

struct WithEmptyUnion {
    
    let union: Union?
    
    init(union: Union?) {
        
        self.union = union
    }
    
    enum Union {
        case UNKNOWN$([String : JSON])
        
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            return .UNKNOWN$(dictionary)
        }
        func write() -> [String : JSON] {
            switch self {
            case .UNKNOWN$(let dictionary):
                return dictionary
            }
        }
    }
    
    static func read(json: JSON) -> WithEmptyUnion {
        return WithEmptyUnion(
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

