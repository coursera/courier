import Foundation
import SwiftyJSON

struct `class`: JSONSerializable {
    
    let `private`: String?
    
    init(
        `private`: String?
    ) {
        self.`private` = `private`
    }
    
    static func read(json: JSON) -> `class` {
        return `class`(
            `private`: json["private"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `private` = self.`private` {
            json["private"] = JSON(`private`)
        }
        return JSON(json)
    }
}
