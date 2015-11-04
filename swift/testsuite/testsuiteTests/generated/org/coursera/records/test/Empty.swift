import Foundation
import SwiftyJSON

public struct Empty: JSONSerializable, Equatable {
    
    public init(
    ) {
    }
    
    public static func read(json: JSON) -> Empty {
        return Empty(
        )
    }
    public func write() -> JSON {
        return [:]
    }
}
public func ==(lhs: Empty, rhs: Empty) -> Bool {
    return (
        true
    )
}
