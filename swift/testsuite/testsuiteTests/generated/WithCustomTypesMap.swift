import Foundation
import SwiftyJSON

public struct WithCustomTypesMap: Serializable, Equatable {
    
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
