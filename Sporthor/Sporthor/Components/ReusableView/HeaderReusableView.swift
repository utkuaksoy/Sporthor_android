//
//  HeaderReusableView.swift
//  Sporthor
//
//  Created by derTurke on 21.03.2025.
//

import UIKit
import ComponentKit

protocol HeaderReusableViewDelegate: AnyObject {
    func didTappedDetail()
}

final class HeaderReusableView: UICollectionReusableView {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, distribution: .equalSpacing)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            font: .bold04Compact)
        return label
    }()
    
    private lazy var detailLabel: CKLabel = {
        let label = CKLabel(delegate: self,
                            textColor: DesignKitColorName.contentSoft600.color,
                            font: .bold04Compact,
                            isUserInteractionEnabled: true,
                            tag: 99)
        return label
    }()
    
    private var stackViewLeadingCons: NSLayoutConstraint!
    private var stackViewTrailingCons: NSLayoutConstraint!
    
    // MARK: - Members
    private weak var delegate: HeaderReusableViewDelegate?
    
    // MARK: - Initializers
    override init(frame: CGRect) {
        super.init(frame: frame)
        prepareUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        prepareUI()
    }
    
    private func prepareUI() {
        stackView.addArrangedSubviews([titleLabel, detailLabel])
        addSubview(stackView)
        
        stackViewLeadingCons = stackView.leadingAnchor.constraint(equalTo: leadingAnchor)
        stackViewTrailingCons = stackView.trailingAnchor.constraint(equalTo: trailingAnchor)
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackViewLeadingCons,
            stackViewTrailingCons,
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    func bind(delegate: HeaderReusableViewDelegate? = nil,
              title: String,
              titleFont: UIFont = .bold04Compact,
              detail: String = "",
              padding: CGFloat = 16) {
        self.delegate = delegate
        titleLabel.text = title
        titleLabel.font = titleFont
        detailLabel.text = detail
        stackViewLeadingCons.constant = padding
        stackViewTrailingCons.constant = -padding
        layoutIfNeeded()
    }
}

// MARK: - CKLabelDelegate
extension HeaderReusableView: CKLabelDelegate {
    func didTapCKLabel(tag: Int) {
        guard let detailText = detailLabel.text,
              !detailText.isEmpty else { return }
        delegate?.didTappedDetail()
    }
}
