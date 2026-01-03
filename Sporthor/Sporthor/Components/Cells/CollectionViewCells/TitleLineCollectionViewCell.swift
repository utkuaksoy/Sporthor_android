//
//  TitleLineCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 7.02.2025.
//

import UIKit
import ComponentKit

final class TitleLineCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var leftLine: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var rightLine: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var label: CKLabel = {
        let label = CKLabel(textAlignment: .center)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    // MARK: - Members
    
    // MARK: - Initialize
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        contentView.addSubview(leftLine)
        contentView.addSubview(label)
        contentView.addSubview(rightLine)
        
        NSLayoutConstraint.activate([
            leftLine.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            leftLine.leadingAnchor.constraint(equalTo: contentView.leadingAnchor),
            leftLine.widthAnchor.constraint(equalTo: contentView.widthAnchor, multiplier: 0.35),
            label.leadingAnchor.constraint(equalTo: leftLine.trailingAnchor),
            label.centerYAnchor.constraint(equalTo: leftLine.centerYAnchor),
            rightLine.leadingAnchor.constraint(equalTo: label.trailingAnchor),
            rightLine.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            rightLine.trailingAnchor.constraint(equalTo: contentView.trailingAnchor),
            rightLine.widthAnchor.constraint(equalTo: contentView.widthAnchor, multiplier: 0.35)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(text: String = "",
              textColor: UIColor = .clear,
              font: UIFont? = .body04Compact,
              lineBackgroundColor: UIColor = .clear,
              lineHeight: CGFloat = 0) {
        label.text = text
        label.textColor = textColor
        label.font = font
        leftLine.backgroundColor = lineBackgroundColor
        rightLine.backgroundColor = lineBackgroundColor
        leftLine.heightAnchor.constraint(equalToConstant: lineHeight).isActive = true
        rightLine.heightAnchor.constraint(equalToConstant: lineHeight).isActive = true
    }
}
