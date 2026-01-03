import ChatKit
import UIKit

protocol MessageProcessing {
    func processImage(_ image: UIImage, completion: @escaping (Result<String, ChatError>) -> Void)
    func processVideo(_ url: URL, completion: @escaping (Result<String, ChatError>) -> Void)
    func processFile(_ url: URL, completion: @escaping (Result<(String), ChatError>) -> Void)
}

final class MessageProcessor: MessageProcessing {
    func processImage(_ image: UIImage, completion: @escaping (Result<String, ChatError>) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let resizedImage = self.resizeImage(
                    image: image,
                    targetSize: CGSize(width: 300, height: 300)
                )

                guard let compressedData = self.compressImage(image: resizedImage, quality: 0.7) else {
                    throw ChatError.imageProcessingFailed
                }
                
                let base64String = compressedData.base64EncodedString()
                guard !base64String.isEmpty else {
                    throw ChatError.imageProcessingFailed
                }
                
                completion(.success(base64String))
            } catch {
                if let chatError = error as? ChatError {
                    completion(.failure(chatError))
                } else {
                    completion(.failure(.imageProcessingFailed))
                }
            }
        }
    }
    
    func processVideo(_ url: URL, completion: @escaping (Result<String, ChatError>) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                let videoData = try Data(contentsOf: url)
                let base64String = try self.processVideoData(videoData)
                
                completion(.success(base64String))
            } catch {
                completion(.failure(.videoProcessingFailed))
            }
        }
    }
    
    func processFile(_ url: URL, completion: @escaping (Result<(String), ChatError>) -> Void) {
        DispatchQueue.global(qos: .userInitiated).async {
            do {
                guard self.validateFileType(url) else {
                    throw ChatError.invalidFileType
                }
                
                let fileData = try Data(contentsOf: url)
                let fileSize = fileData.count
                let maxSize: Int = 50 * 1024 * 1024
                
                guard fileSize <= maxSize else {
                    throw ChatError.fileSizeTooLarge
                }
                
                let base64String = fileData.base64EncodedString()
                guard !base64String.isEmpty else {
                    throw ChatError.fileProcessingFailed
                }
                
                completion(.success((base64String)))
            } catch {
                if let chatError = error as? ChatError {
                    completion(.failure(chatError))
                } else {
                    completion(.failure(.fileProcessingFailed))
                }
            }
        }
    }
    
    private func resizeImage(image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        let widthRatio  = targetSize.width  / size.width
        let heightRatio = targetSize.height / size.height
        let ratio = min(widthRatio, heightRatio)
        
        let newSize = CGSize(
            width: size.width * ratio,
            height: size.height * ratio
        )
        
        let rect = CGRect(
            x: 0,
            y: 0,
            width: newSize.width,
            height: newSize.height
        )
        
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage ?? image
    }
    
    private func compressImage(image: UIImage, quality: CGFloat) -> Data? {
        return image.jpegData(compressionQuality: quality)
    }
    
    private func processVideoData(_ videoData: Data) throws -> String {
        let maxSize: Int = 50 * 1024 * 1024
        
        if videoData.count > maxSize {
            throw ChatError.videoProcessingFailed
        }
        
        let base64String = videoData.base64EncodedString()
        guard !base64String.isEmpty else {
            throw ChatError.videoProcessingFailed
        }
        
        return base64String
    }
    
    private func validateVideoURL(_ url: URL) -> Bool {
        let allowedTypes = ["mp4", "mov", "m4v"]
        return allowedTypes.contains(url.pathExtension.lowercased())
    }
    
    private func validateFileType(_ url: URL) -> Bool {
        let allowedTypes = ["pdf", "doc", "docx", "xls", "xlsx", "txt", "zip", "rar", "png", "jpeg"]
        return allowedTypes.contains(url.pathExtension.lowercased())
    }
} 
