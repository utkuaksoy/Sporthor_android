//
//  ListTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 1.07.2025.
//
//

import UIKit
import ComponentKit

final class ListTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ListTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! ListTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .plain)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 16, right: 0)
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
        tableView.removeEmptyCell()
        return tableView
    }()
    
    private lazy var deleteSubmitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Seçili Antreman Grubunu Sil",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.isHidden = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private var deleteSubmitButtonTopCons: NSLayoutConstraint!
    private var deleteSubmitButtonBottomCons: NSLayoutConstraint!
    private var deleteSubmitButtonHeightCons: NSLayoutConstraint!
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.navigationItem.setHidesBackButton(true, animated: false)
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
            navCon.navigationBar.titleTextAttributes = [
                .foregroundColor: UIColor.black,
                .font: UIFont.bold03Compact
            ]
        }
        presenter.viewWillAppear()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        navigationController?.navigationItem.setHidesBackButton(false, animated: false)
    }
    
    // MARK: - Custom Methods
}

// MARK: - ListTrainingGroupPresenterDelegate
extension ListTrainingGroupViewController: ListTrainingGroupPresenterDelegate {
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackChevronLeft = true
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        view.addSubview(deleteSubmitButton)
        
        deleteSubmitButtonTopCons = deleteSubmitButton.topAnchor.constraint(equalTo: tableView.bottomAnchor)
        deleteSubmitButtonBottomCons = deleteSubmitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        deleteSubmitButtonHeightCons = deleteSubmitButton.heightAnchor.constraint(equalToConstant: 0)
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            deleteSubmitButtonTopCons,
            deleteSubmitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            deleteSubmitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            deleteSubmitButtonBottomCons,
            deleteSubmitButtonHeightCons
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func changeDeleteSubmitButtonHiddenState(_ isHidden: Bool) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            if let navCon = navigationController as? CustomNavigationController {
                navCon.isRemoveTextRightBarButtonItem = true
                navCon.isTextRightBarButtonItem = (
                    text: isHidden ? presenter.trainingGroups.isEmpty ? "" : "Sil" : "İptal",
                    textColor: DesignKitColorName.contentStrong900.color,
                    font: .body03Compact
                )
            }
            self.deleteSubmitButton.isHidden = isHidden
            self.deleteSubmitButtonTopCons.constant = isHidden ? 0 : 8
            self.deleteSubmitButtonBottomCons.constant = isHidden ? 0 : -16
            self.deleteSubmitButtonHeightCons.constant = isHidden ? 0 : 46
        }
    }
}

// MARK: - UITableViewDataSource
extension ListTrainingGroupViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.trainingGroups.isEmpty ? 1 : presenter.trainingGroups.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if presenter.trainingGroups.isEmpty {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(image: Asset.infoAlert.image, description: "Antreman grubunuz bulunmamaktadır.")
            return cell
        } else {
            let model = presenter.trainingGroups[indexPath.row]
            let cell = TrainingGroupTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(with: model,
                           rightImage: presenter.isDeleted ? model.isSelected ? Asset.checked.image : Asset.unchecked.image : Asset.chevronRightGrey.image)
            return cell
        }
    }
}

// MARK: - UITableViewDelegate
extension ListTrainingGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

// MARK: - CustomNavigationControllerDelegate
extension ListTrainingGroupViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.didTappedNavigationButton(type)
    }
}

extension ListTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.ckButtonDidTap(tag: tag)
    }
}
        
