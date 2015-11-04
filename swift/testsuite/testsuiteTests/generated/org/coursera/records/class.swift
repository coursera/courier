import Foundation
import SwiftyJSON

public struct `class`: JSONSerializable {
    
    public let `private`: String?
    
    public init(
        `private`: String?
    ) {
        self.`private` = `private`
    }
    
    public static func read(json: JSON) -> `class` {
        return `class`(
            `private`: json["private"].string
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `private` = self.`private` {
            json["private"] = JSON(`private`)
        }
        return JSON(json)
    }
}
