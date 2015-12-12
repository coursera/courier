import Foundation
import SwiftyJSON

public struct WithCustomArrayTestId: Serializable {
    
    public let array: [Int]?
    
    public init(
        array: [Int]?
    ) {
        self.array = array
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomArrayTestId {
        return WithCustomArrayTestId(
            array: try json["array"].optional(.Array).array.map { try $0.map { try $0.required(.Number).intValue } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let array = self.array {
            dict["array"] = array
        }
        return dict
    }
}
