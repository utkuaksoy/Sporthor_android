//
//  ImageTitleInfoRightImageTableViewCell.swift
//  Sporthor
//
//  Created by GÜRHAN YUVARLAK on 31.10.2025.
//

import UIKit
import ComponentKit

final class ImageTitleInfoRightImageTableViewCell: UITableViewCell {
    // MARK: - UI Elements
    private lazy var ckImageTitleInfoRightImageView: CKImageTitleInfoRightImageView = {
        let view = CKImageTitleInfoRightImageView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    // MARK: - Initialize
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    // MARK: - Custom Methods
    private func prepareUI() {
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(ckImageTitleInfoRightImageView)
        
        NSLayoutConstraint.activate([
            ckImageTitleInfoRightImageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            ckImageTitleInfoRightImageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            ckImageTitleInfoRightImageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            ckImageTitleInfoRightImageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func configure(image: String? = nil,
                          placeholderImage: UIImage? = nil,
                          title: String? = nil,
                          info: String? = nil,
                          rightImage: UIImage? = nil) {
        ckImageTitleInfoRightImageView.bind(image: image,
                                            placeholderImage: placeholderImage ?? Asset.errorUserImage.image,
                                            title: title,
                                            info: info,
                                            rightImage: rightImage)
    }
}
