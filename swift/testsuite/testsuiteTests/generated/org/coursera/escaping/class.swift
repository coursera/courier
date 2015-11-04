import Foundation
import SwiftyJSON

public struct `class`: JSONSerializable {
    
    public init(
    ) {
    }
    
    public static func read(json: JSON) -> `class` {
        return `class`(
        )
    }
    public func write() -> JSON {
        return [:]
    }
}
