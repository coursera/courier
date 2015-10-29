import Foundation
import SwiftyJSON

struct `class`: JSONSerializable {
    
    init(
    ) {
    }
    
    static func read(json: JSON) -> `class` {
        return `class`(
        )
    }
    func write() -> JSON {
        return [:]
    }
}
