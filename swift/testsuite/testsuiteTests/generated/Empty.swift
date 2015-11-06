import Foundation
import SwiftyJSON

public struct Empty: Serializable, Equatable {
    
    public init(
    ) {
    }
    
    public static func readJSON(json: JSON) throws -> Empty {
        return Empty(
        )
    }
    public func writeData() -> [String: AnyObject] {
        return [:]
    }
}
public func ==(lhs: Empty, rhs: Empty) -> Bool {
    return (
        true
    )
}
