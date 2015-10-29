import Foundation
import SwiftyJSON

struct KeywordEscaping {
    
    let `default`: String?
    
    init(`default`: String?) {
        
        self.`default` = `default`
    }
    
    static func read(json: JSON) -> KeywordEscaping {
        return KeywordEscaping(
        `default`:
        json["default"].string
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let `default` = self.`default` {
            json["default"] = JSON(`default`)
        }
        
        return json
    }
}
