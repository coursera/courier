import Foundation
import SwiftyJSON

struct Empty: Equatable {
    
    init() {
    }
    
    static func read(json: JSON) -> Empty {
        return Empty(
        )
    }
    func write() -> [String : JSON] {
        return [:]
    }
}
func ==(lhs: Empty, rhs: Empty) -> Bool {
    return (
    true)
}
