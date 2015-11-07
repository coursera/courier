import Foundation
import SwiftyJSON

public struct WithDateTime: Serializable {
    
    public let time: Int?
    
    public init(
        time: Int?
    ) {
        self.time = time
    }
    
    public static func readJSON(json: JSON) throws -> WithDateTime {
        return WithDateTime(
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
