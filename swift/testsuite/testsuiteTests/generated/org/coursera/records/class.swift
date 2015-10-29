import Foundation
import SwiftyJSON

struct `class` {
    
    let `private`: String?
    
    init(`private`: String?) {
        
        self.`private` = `private`
    }
    
    static func read(json: JSON) -> `class` {
        return `class`(
        `private`:
        json["private"].string
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let `private` = self.`private` {
            json["private"] = JSON(`private`)
        }
        
        return json
    }
}
