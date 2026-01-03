//
//  DocumentCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 15.04.2025.
//

import AutoLayout
import AVKit
import CryptoKit
import ChatKit
import ComponentKit
import ComponentBaseKit
import DesignKit
import QuickLook
import UIKit

protocol DocumentCollectionViewCellDelegate: AnyObject {
    func didTapDocument(fileUrl: URL?, fileExtension: String?)
}

final class DocumentCollectionViewCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Components
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(didTappedFileButton))
        view.addGestureRecognizer(tapGesture)
        view.isUserInteractionEnabled = true
        return view
    }()
    
    private lazy var containerStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [iconImageView, textStackView])
        stack.axis = .horizontal
        stack.spacing = 12
        stack.alignment = .center
        stack.distribution = .fill
        return stack
    }()

    private lazy var iconImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        return imageView
    }()
    
    private lazy var textStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, sizeLabel])
        stack.axis = .vertical
        stack.spacing = 4
        stack.alignment = .leading
        stack.distribution = .fill
        return stack
    }()
    
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = ColorName.contentStrong900.color
        return label
    }()
    
    private lazy var sizeLabel: UILabel = {
        let label = UILabel()
        label.font = .body04Compact
        label.textColor = ColorName.contentSoft600.color
        return label
    }()
    
    // MARK: - Private Properties
    
    private weak var delegate: DocumentCollectionViewCellDelegate?
    private var fileUrl: URL?
    private var fileExtension: String?
    
    // MARK: - Initialize

    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    
    private func setupUI() {
        contentView.addSubview(containerView) {
            $0.pin(edges: .all, to: contentView, with: .init(top: .zero, left: 16, bottom: .zero, right: 16))
        }
        containerView.addSubview(containerStackView) {
            $0.pin(to: containerView)
        }
        iconImageView.layout {
            $0.width == 48
            $0.height == 48
        }
    }
    
    // MARK: - Configure

    func configure(delegate: DocumentCollectionViewCellDelegate?, with item: ChatMessageResponse) {
        self.delegate = delegate
        if let content = item.content, let fileUrl = base64ToFile(with: content), let fileType = item.fileExtension {
            self.fileUrl = fileUrl
            self.fileExtension = fileType
            nameLabel.text = getDisplayName(for: fileUrl)
            sizeLabel.text = getFileSize(for: fileUrl)
            iconImageView.image = getFileIcon(for: fileType)?.withRenderingMode(.alwaysTemplate)
        }
    }
    
    @objc
    private func didTappedFileButton() {
        delegate?.didTapDocument(fileUrl: fileUrl, fileExtension: fileExtension)
    }
}

private extension DocumentCollectionViewCell {
    func base64ToFile(with content: String, fileExtension: String = "bin") -> URL? {
        guard let data = Data(base64Encoded: content) else {
            return nil
        }
        
        let hash = SHA256.hash(data: data).compactMap { String(format: "%02x", $0) }.joined()
        let fileName = "\(hash).\(fileExtension)"
        
        let tempDirectoryURL = FileManager.default.temporaryDirectory
        let fileURL = tempDirectoryURL.appendingPathComponent(fileName)
        
        if FileManager.default.fileExists(atPath: fileURL.path) {
            return fileURL
        }
        
        do {
            try data.write(to: fileURL)
            return fileURL
        } catch {
            return nil
        }
    }
    
    func getDisplayName(for fileURL: URL) -> String {
        do {
            let resourceValues = try fileURL.resourceValues(forKeys: [.localizedNameKey])
            if let name = resourceValues.localizedName {
                return name
            }
        } catch {
            print("⚠️ Dosya adı alınamadı: \(error)")
        }
        return fileURL.lastPathComponent
    }
    
    func getFileIcon(for fileType: String) -> UIImage? {
        switch fileType.lowercased() {
        case "pdf": return UIImage(systemName: "doc.richtext")
        case "png", "jpg", "jpeg": return UIImage(systemName: "photo.fill")
        case "zip": return UIImage(systemName: "archivebox.fill")
        case "mp4", "mov": return UIImage(systemName: "film")
        default: return UIImage(systemName: "doc.fill")
        }
    }
    
    func getFileSize(for fileURL: URL) -> String {
        do {
            let resourceValues = try fileURL.resourceValues(forKeys: [.fileSizeKey])
            if let fileSize = resourceValues.fileSize {
                return ByteCountFormatter.string(fromByteCount: Int64(fileSize), countStyle: .file)
            }
        } catch {
            return "Bilinmeyen Boyut"
        }
        return ""
    }
}
