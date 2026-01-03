import Foundation

enum MockDataError: Error {
    case fileNotFound
    case invalidData
    case decodingError
}

final class MockDataService {
    static func loadMockData<T: Decodable>(filename: String) async throws -> T {
        guard let url = Bundle.main.url(forResource: filename, withExtension: "json", subdirectory: "Mock") else {
            throw MockDataError.fileNotFound
        }
        
        let data = try Data(contentsOf: url)
        
        do {
            let decoder = JSONDecoder()
            return try decoder.decode(T.self, from: data)
        } catch {
            throw MockDataError.decodingError
        }
    }
} 