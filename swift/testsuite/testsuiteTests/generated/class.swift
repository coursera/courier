import Foundation
import SwiftyJSON

public struct `class`: Serializable {
    
    public init(
    ) {
    }
    
    public static func readJSON(json: JSON) throws -> `class` {
        return `class`(
        )
    }
    public func writeData() -> [String: AnyObject] {
        return [:]
    }
}
