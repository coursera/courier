import Foundation
import SwiftyJSON

struct WithEmptyUnion: JSONSerializable {
    
    let union: Union?
    
    init(
        union: Union?
    ) {
        self.union = union
    }
    
    enum Union: JSONSerializable {
        case UNKNOWN$([String : JSON])
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            return .UNKNOWN$(dictionary)
        }
        func write() -> JSON {
            switch self {
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    static func read(json: JSON) -> WithEmptyUnion {
        return WithEmptyUnion(
            union: json["union"].json.map { Union.read($0) }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let union = self.union {
            json["union"] = union.write()
        }
        return JSON(json)
    }
}

