//
//  ImageViewTableViewCell.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//

import UIKit

final class ImageViewTableViewCell: UITableViewCell {
    
    // MARK: - UI Elements
    private lazy var customImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Initializers
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    // MARK: - Setup UI
    private func setupUI() {
        contentView.addSubview(customImageView)
        NSLayoutConstraint.activate([
            customImageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            customImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            customImageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            customImageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Bind Methods
    func bind(image: String?) {
        customImageView.image = UIImage(named: image ?? "")
    }
    
    func bind(with urlString: String?) {
        customImageView.setImage(with: urlString)
    }
    
    func bind(_ image: UIImage,
              clipsToBounds: Bool = false,
              contentMode: ContentMode = .scaleAspectFill) {
        customImageView.image = image
        customImageView.clipsToBounds = clipsToBounds
        customImageView.contentMode = contentMode
    }
}
