import Foundation
import SwiftyJSON

public struct Empty: JSONSerializable, DataTreeSerializable, Equatable {
    
    public init(
    ) {
    }
    
    public static func readJSON(json: JSON) -> Empty {
        return Empty(
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> Empty {
        return readJSON(JSON(data))
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
