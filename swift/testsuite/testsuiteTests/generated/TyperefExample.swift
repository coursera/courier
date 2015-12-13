import Foundation
import SwiftyJSON

public struct TyperefExample: Serializable {
    
    public let time: Int?
    
    public init(
        time: Int? = 1430849546000
    ) {
        self.time = time
    }
    
    public static func readJSON(json: JSON) throws -> TyperefExample {
        return TyperefExample(
            time: try json["time"].optional(.Number).int
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let time = self.time {
            dict["time"] = time
        }
        return dict
    }
}
