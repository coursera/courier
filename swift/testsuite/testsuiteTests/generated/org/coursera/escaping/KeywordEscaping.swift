import Foundation
import SwiftyJSON

struct KeywordEscaping: JSONSerializable {
    
    let `default`: String?
    
    init(
        `default`: String?
    ) {
        self.`default` = `default`
    }
    
    static func read(json: JSON) -> KeywordEscaping {
        return KeywordEscaping(
            `default`: json["default"].string
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `default` = self.`default` {
            json["default"] = JSON(`default`)
        }
        return JSON(json)
    }
}
