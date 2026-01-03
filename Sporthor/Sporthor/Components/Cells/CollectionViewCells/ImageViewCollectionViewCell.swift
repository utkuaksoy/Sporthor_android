//
//  ImageViewCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 25.03.2025.
//

import UIKit

final class ImageViewCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    // MARK: - Initilaizers
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    private func setupUI() {
        contentView.addSubview(imageView)
        NSLayoutConstraint.activate([
            imageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            imageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            imageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(image: String?) {
        imageView.image = UIImage(named: image ?? "")
    }
    
    func bind(with urlString: String?) {
        imageView.setImage(with: urlString)
    }
    
    func bind(_ image: UIImage,
              clipsToBounds: Bool = false,
              contentMode: ContentMode = .scaleAspectFill) {
        imageView.image = image
        imageView.clipsToBounds = clipsToBounds
        imageView.contentMode = contentMode
    }
}
