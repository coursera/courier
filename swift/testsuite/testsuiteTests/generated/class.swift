import Foundation
import SwiftyJSON

public struct `class`: Serializable {
    
    public let `private`: String?
    
    public init(
        `private`: String?
    ) {
        self.`private` = `private`
    }
    
    public static func readJSON(json: JSON) throws -> `class` {
        return `class`(
            `private`: try json["private"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let `private` = self.`private` {
            dict["private"] = `private`
        }
        return dict
    }
}
