import Foundation
import SwiftyJSON

struct Empty: JSONSerializable, Equatable {
    
    init(
    ) {
    }
    
    static func read(json: JSON) -> Empty {
        return Empty(
        )
    }
    func write() -> JSON {
        return [:]
    }
}
func ==(lhs: Empty, rhs: Empty) -> Bool {
    return (
        true
    )
}
