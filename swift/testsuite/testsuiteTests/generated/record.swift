import Foundation
import SwiftyJSON

public struct record: Serializable {
    
    public init(
    ) {
    }
    
    public static func readJSON(json: JSON) throws -> record {
        return record(
        )
    }
    public func writeData() -> [String: AnyObject] {
        return [:]
    }
}
