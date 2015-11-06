import Foundation
import SwiftyJSON

public struct WithCustomTypesMap: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let ints: [String: Int]?
    
    public init(
        ints: [String: Int]?
    ) {
        self.ints = ints
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomTypesMap {
        return WithCustomTypesMap(
            ints: json["ints"].dictionary.map { $0.mapValues { $0.intValue } }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithCustomTypesMap {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let ints = self.ints {
            dict["ints"] = ints
        }
        return dict
    }
}
public func ==(lhs: WithCustomTypesMap, rhs: WithCustomTypesMap) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        true
    )
}
