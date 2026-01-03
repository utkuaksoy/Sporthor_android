//
//  DocumentCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import UIKit
import AVKit
import QuickLook
import UniformTypeIdentifiers

final class ChatPreviewCoordinator: NSObject {
    
    // MARK: - Properties
    private weak var presenter: UIViewController?
    private var fileURL: URL?
    private var fileType: String?
    
    private lazy var previewController: QLPreviewController = {
        let controller = QLPreviewController()
        controller.dataSource = self
        controller.modalPresentationStyle = .fullScreen
        return controller
    }()

    // MARK: - Init
    init(presenter: UIViewController) {
        self.presenter = presenter
    }

    // MARK: - Public
    func start(with fileURL: URL, fileType: String) {
        guard let presenter else { return }

        self.fileType = fileType.lowercased()
        self.fileURL = prepareURL(fileURL, fileType: fileType)

        guard let safeURL = self.fileURL else {
            showError("Dosya hazırlanırken bir hata oluştu")
            return
        }

        guard isAllowedFileType(url: safeURL, fileType: fileType) else {
            showError("Bu dosya formatı desteklenmiyor")
            return
        }

        switch fileType.lowercased() {
        case "jpg", "jpeg", "png":
            presentQuickLook(for: safeURL)
        case "mp4", "mov":
            presentVideoPlayer(for: safeURL)
        default:
            presentQuickLook(for: safeURL)
        }
    }

    // MARK: - Private Methods
    
    private func prepareURL(_ url: URL, fileType: String) -> URL? {
        guard url.pathExtension == "bin" else { return url }

        let newExtension = fileType.lowercased()
        let newPath = (url.deletingPathExtension().path as NSString).appendingPathExtension(newExtension)!
        let newURL = URL(fileURLWithPath: newPath)

        if FileManager.default.fileExists(atPath: newURL.path) {
            return newURL
        }

        do {
            try FileManager.default.copyItem(at: url, to: newURL)
            return newURL
        } catch {
            print("❌ Dosya kopyalanamadı: \(error)")
            return nil
        }
    }

    private func presentQuickLook(for url: URL) {
        previewController.reloadData()
        presenter?.present(previewController, animated: true)
    }

    private func presentVideoPlayer(for url: URL) {
        guard FileManager.default.fileExists(atPath: url.path) else {
            showError("Video dosyası bulunamadı")
            return
        }

        let player = AVPlayer(url: url)
        let vc = AVPlayerViewController()
        vc.player = player

        presenter?.present(vc, animated: true) {
            player.play()
        }
    }

    private func showError(_ message: String) {
        let alert = UIAlertController(title: "Hata", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Tamam", style: .default))
        presenter?.present(alert, animated: true)
    }

    private func isAllowedFileType(url: URL, fileType: String? = nil) -> Bool {
        var allowedTypes: [UTType] = [
            .pdf, .jpeg, .png, .plainText, .zip, .rtf, .data, .image
        ]
        
        ["doc", "docx", "xls", "xlsx", "ppt", "pptx"].forEach {
            if let type = UTType(filenameExtension: $0) {
                allowedTypes.append(type)
            }
        }

        let ext = url.pathExtension.lowercased()
        if let utType = UTType(filenameExtension: ext) {
            return allowedTypes.contains(utType)
        }

        if let mime = try? url.resourceValues(forKeys: [.contentTypeKey]).contentType {
            return allowedTypes.contains(mime)
        }

        return false
    }
}

// MARK: - QLPreviewControllerDataSource
extension ChatPreviewCoordinator: QLPreviewControllerDataSource {
    func numberOfPreviewItems(in controller: QLPreviewController) -> Int {
        return fileURL == nil ? 0 : 1
    }

    func previewController(_ controller: QLPreviewController, previewItemAt index: Int) -> QLPreviewItem {
        guard let fileURL else {
            fatalError("Dosya URL'si bulunamadı")
        }
        return fileURL as QLPreviewItem
    }
}
