//
//  SeperatorCollectionViewCell.swift
//  Sporthor
//
//  Created by derTurke on 22.02.2025.
//

import UIKit

final class SeperatorCollectionViewCell: UICollectionViewCell {
    // MARK: - UI Elements
    private lazy var seperatorView: UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private var widthConstraint: NSLayoutConstraint!
    private var heightConstraint: NSLayoutConstraint!
    
    // MARK: - Initialize
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
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        contentView.addSubview(seperatorView)
        
        widthConstraint = seperatorView.widthAnchor.constraint(equalToConstant: 0)
        heightConstraint = seperatorView.heightAnchor.constraint(equalToConstant: 0)
        
        NSLayoutConstraint.activate([
            seperatorView.centerYAnchor.constraint(equalTo: centerYAnchor),
            seperatorView.centerXAnchor.constraint(equalTo: centerXAnchor),
            widthConstraint,
            heightConstraint
        ])
    }
    
    // MARK: - Custom Methods
    func bind(color: UIColor, width: CGFloat, height: CGFloat) {
        seperatorView.backgroundColor = color
        widthConstraint.constant = width
        heightConstraint.constant = height
        layoutIfNeeded()
    }
}
