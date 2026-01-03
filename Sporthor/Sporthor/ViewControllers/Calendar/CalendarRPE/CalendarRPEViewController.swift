//
//  CalendarRPEViewController.swift
//  Sporthor
//
//  Created by derTurke on 25.07.2025.
//
//

import UIKit
import PanModal
import ComponentKit

final class CalendarRPEViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarRPEPresenterProtocol {
        get { return self.basePresenter as! CalendarRPEPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(text: "RPE Puanla", textColor: .black, textAlignment: .center , font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.separatorInset = .init(top: 0, left: 16, bottom: 0, right: 16)
        tableView.separatorColor = DesignKitColorName.borderSub300.color
        return tableView
    }()
    
    private lazy var starStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    distribution: .equalCentering,
                                    alignment: .center,
                                    spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Kaydet",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
    @objc private func tappedStarImageView(_ sender: UITapGestureRecognizer) {
        guard let tappedImageView = sender.view as? UIImageView else { return }
        let selectedIndex = tappedImageView.tag
        presenter.didSelectStar(at: selectedIndex)
    }
}

// MARK: - CalendarRPEPresenterDelegate
extension CalendarRPEViewController: CalendarRPEPresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(tableView)
        view.addSubview(starStackView)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            tableView.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 32),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            starStackView.topAnchor.constraint(equalTo: tableView.bottomAnchor, constant: 16),
            starStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            starStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            starStackView.bottomAnchor.constraint(equalTo: submitButton.topAnchor, constant: -32),
            
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func prepareStar(selectedStar: Int) {
        if starStackView.arrangedSubviews.isEmpty {
            for i in 1...10 {
                let imageView = UIImageView(image: i <= selectedStar ? Asset.starFill.image : Asset.star.image)
                imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
                imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
                imageView.tag = i
                imageView.isUserInteractionEnabled = true
                imageView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(tappedStarImageView(_:))))
                starStackView.addArrangedSubview(imageView)
            }
            return
        }

        for (index, view) in starStackView.arrangedSubviews.enumerated() {
            guard let imageView = view as? UIImageView else { continue }

            let newImage = (index + 1) <= selectedStar ? Asset.starFill.image : Asset.star.image
            if imageView.image != newImage {
                imageView.alpha = 0.0
                imageView.transform = CGAffineTransform(scaleX: 0.5, y: 0.5)
                imageView.image = newImage

                UIView.animate(withDuration: 0.3) {
                    imageView.alpha = 1.0
                    imageView.transform = .identity
                }
            }
        }
    }

}

extension CalendarRPEViewController: UITableViewDataSource, UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.rpeModel.count + 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = RPETableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(RPEModel(point: "Puan", title: "RPE Açıklama", subtitle: "", star: ""))
            return cell
        } else {
            let cell = RPETableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(presenter.rpeModel[indexPath.row - 1])
            return cell
        }
    }
}

extension CalendarRPEViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}

extension CalendarRPEViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    var longFormHeight: PanModalHeight {
        return .maxHeight
    }
}

