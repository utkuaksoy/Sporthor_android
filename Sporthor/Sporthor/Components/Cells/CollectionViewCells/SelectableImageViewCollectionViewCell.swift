//
//  SelectableImageViewCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 22.04.2025.
//

import UIKit
import CommonKit
import ComponentKit

final class SelectableImageViewCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var shadowView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var selectableImage: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.isHidden = true
        return imageView
    }()
    
    private lazy var selectedIndexLabel: CKLabel = {
        let label = CKLabel(textColor: .white, font: .bold04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.isHidden = true
        return label
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
        contentView.addSubview(shadowView)
        contentView.addSubview(selectableImage)
        contentView.addSubview(selectedIndexLabel)
        NSLayoutConstraint.activate([
            imageView.topAnchor.constraint(equalTo: contentView.topAnchor),
            imageView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            imageView.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            imageView.bottomAnchor.constraint(equalTo: contentView.bottomAnchor),
            
            shadowView.topAnchor.constraint(equalTo: imageView.topAnchor),
            shadowView.leadingAnchor.constraint(equalTo: imageView.leadingAnchor),
            shadowView.trailingAnchor.constraint(equalTo: imageView.trailingAnchor),
            shadowView.bottomAnchor.constraint(equalTo: imageView.bottomAnchor),
            
            selectableImage.topAnchor.constraint(equalTo: shadowView.topAnchor, constant: 8),
            selectableImage.trailingAnchor.constraint(equalTo: shadowView.trailingAnchor, constant: -8),
            selectableImage.widthAnchor.constraint(equalToConstant: 24),
            selectableImage.heightAnchor.constraint(equalToConstant: 24),
            
            selectedIndexLabel.centerXAnchor.constraint(equalTo: selectableImage.centerXAnchor),
            selectedIndexLabel.centerYAnchor.constraint(equalTo: selectableImage.centerYAnchor)
        ])
        
    }
    override func prepareForReuse() {
        super.prepareForReuse()
        imageView.image = nil
        selectableImage.image = nil
        selectedIndexLabel.text = nil
        selectedIndexLabel.isHidden = true
        selectableImage.isHidden = true
        shadowView.backgroundColor = .clear
    }
    
    // MARK: - Custom Methods
    
    func bind(_ model: AssetModel, multipleSelected: Bool) {
        guard let asset = model.asset else { return }
        let identifier = asset.localIdentifier
        imageView.image = nil
        
        let scale = UIScreen.main.scale
        let cellSize = (UIScreen.main.bounds.width - 6) / 3
        let targetSize = CGSize(width: cellSize * scale, height: cellSize * scale)
        Task {
            @MainActor in
            guard let image = await BaseHelper.shared.requestImage(for: asset, targetSize: targetSize) else { return }
            if identifier == model.asset?.localIdentifier {
                imageView.image = image
            }
        }
        shadowView.backgroundColor = model.isSelected ? DesignKitColorName.whiteOpacity40.color : .clear
        
        if multipleSelected {
            selectableImage.isHidden = false
            selectableImage.image = model.isSelected ? Asset.selectableBlack.image : Asset.selectableWhite.image
            
            selectedIndexLabel.isHidden = false
            selectedIndexLabel.text = "\(model.index ?? 0)"
        } else {
            selectableImage.isHidden = true
            selectedIndexLabel.isHidden = true
        }
    }

}

